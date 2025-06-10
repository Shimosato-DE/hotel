/**
 * 
 */



const imageInput = document.getElementById('imageFile');
const imagePreview = document.getElementById('imagePreview');
 
imageInput.addEventListener('change', () => {
 
    if (imageInput.files && imageInput.files[0]) { //ファイルが選択されているか判定している
        let fileReader = new FileReader();
        fileReader.onload = () => {
            imagePreview.innerHTML = `<img src="${fileReader.result}" class="mb-3">`;
        };
 
        fileReader.readAsDataURL(imageInput.files[0]);
 
    } else {
        imagePreview.innerHTML = '';
    }
 
});

//fileReader.onloadの関数内で imagePreview.innerHTML に画像タグをつけてます