package com.example.samuraitravel.service;

import java.util.Map;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.samuraitravel.form.ReservationRegisterForm;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionRetrieveParams;

@Service
public class StripeService {

	@Value("${stripe.api-key}")
	private String stripeApiKey;

	private final ReservationService reservationService;

	public StripeService(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	// Stripeに送信する支払い情報をセッションとして作成する
	// 戻り値：セッションID 引数：
	public String createStripeSession(String houseName, ReservationRegisterForm reservationRegisterForm,
			HttpServletRequest httpServletRequest) {

		Stripe.apiKey = stripeApiKey;
		
		
		//現在のHTTPリクエストの完全なURLを取得
		String requestUrl = new String(httpServletRequest.getRequestURL());
		
		
		//Stripe Checkout Sessionを作成するためのパラメータを定義するビルダーを開始
		SessionCreateParams params = SessionCreateParams.builder()
				
				//決済時に利用できる支払い方法を追加(ここではカード)
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				
				//決済対象の「商品ラインアイテム」を追加します。この部分はStripe決済で何を購入するかを定義します。
				.addLineItem(SessionCreateParams.LineItem.builder().setPriceData(SessionCreateParams.LineItem.PriceData
						.builder()
						.setProductData(
								SessionCreateParams.LineItem.PriceData.ProductData.builder().setName(houseName).build())
						.setUnitAmount((long) reservationRegisterForm.getAmount()).setCurrency("jpy").build())
						.setQuantity(1L).build())
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl(
						requestUrl.replaceAll("/houses/[0-9]+/reservations/confirm", "") + "/reservations?reserved")
				.setCancelUrl(requestUrl.replace("/reservations/confirm", ""))

				.setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder()
						.putMetadata("houseId", reservationRegisterForm.getHouseId().toString())
						.putMetadata("userId", reservationRegisterForm.getUserId().toString())
						.putMetadata("checkinDate", reservationRegisterForm.getCheckinDate())
						.putMetadata("checkoutDate", reservationRegisterForm.getCheckoutDate())
						.putMetadata("numberOfPeople", reservationRegisterForm.getNumberOfPeople().toString())
						.putMetadata("amount", reservationRegisterForm.getAmount().toString()).build())
				.build();

		try {

			Session session = Session.create(params);
			return session.getId();

		} catch (StripeException e) {

			e.printStackTrace();
			return "";

		}

	}

	// セッションから予約情報を取得し、ReservationServiceクラスを介してデータベースに登録する
	public void processSessionCompleted(Event event) {
		
		//Eventからイベントデータを取得して格納。(EventのデータペイロードをStripeObjectにデシリアライズ(Javaオブジェクトとして変換)
		//変換できない場合に備え、Optionalでラップ
		Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
		
		// ★★★ ここにログを追加して、Optionalが空かどうかを確認 ★★★
		if (optionalStripeObject.isPresent()) {
		    System.out.println("processSessionCompleted: optionalStripeObject.isPresent() is TRUE. Proceeding to process StripeObject.");
		} else {
		    System.err.println("processSessionCompleted: optionalStripeObject.isPresent() is FALSE. DataObject could not be deserialized or is empty. This prevents the try block from executing.");
            System.err.println("Raw Event Data JSON: " + event.getData()); // デシリアライズできなかった生データをログ出力
            // 重要なのは、event.getData().getObject() の中身がnullかどうか。
            // event.getData().getObject() 自体は null ではなく、StripeObjectを保持していなければ null を返す
            // この場合、data.objectがnullになっている可能性がある
            if (event.getData() != null && event.getData().getObject() == null) {
                System.err.println("Event Data Object is NULL. This is why Optional is empty.");
            }
		}
		
		//optionalStripeObjectに値が存在する場合のみ、処理を実行
		//stripeObjectは、EventデータからデシリアライズされたStripeObjectのインスタンス
		optionalStripeObject.ifPresent(stripeObject -> {
			
			//StripeObjectを、Sessionオブジェクトにキャスト
			Session session = (Session) stripeObject;
			
			
			//Stripe APIからセッション情報を再取得するためのパラメータ（SessionRetrieveParams）を構築
			// .addExpand("payment_intent") は、取得するセッション情報に紐づくPaymentIntentオブジェクトも一緒に取得するように指定
			// PaymentIntentには、決済に関するメタデータ（追加情報）が含まれており、これを使って予約情報をDBに保存を行うことになる
			SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("payment_intent").build();
			
			
			//Stripe API呼び出し中に発生する可能性のあるStripeException（ネットワークエラー、APIエラーなど）を捕捉するためのtryブロック
			try {
				System.out.println("tryブロック実行");
				session = Session.retrieve(session.getId(), params, null);
				Map<String, String> paymentIntentObject = session.getPaymentIntentObject().getMetadata();
				System.out.println("登録情報:" + paymentIntentObject);
				reservationService.create(paymentIntentObject);

			} catch (StripeException e) {
				e.printStackTrace();
			}
		});
	}
}
