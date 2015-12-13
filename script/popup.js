addLoadEvent(prepareLinks);
addLoadEvent(prepareGallery);

function addLoadEvent(func){
	var oldload = window.onload;
	if(typeof window.onload != 'function'){ // 第一次
		window.onload = func;
	}else{
		window.onload = function(){ // 后面就add
			oldload();
			func();
		}
	}
}

function prepareLinks(){
	var links = document.getElementsByTagName("a");
	for(var i = 0; i < links.length; i++){
		if(i == 0 || i == 1){
			links[i].onclick = function(){
				popUp(this.getAttribute("href")); // must be this
				return false;
			}
		}
	}
	

}

function prepareGallery(){
	var gallery = document.getElementById("gallery");
	var links = gallery.getElementsByTagName("a");
		for(var i = 0; i < links.length; i++){
		links[i].onclick = function(){
			return !showPic(this);
		}
		// links[i].onkeypress = links[i].onclick;
	}
}

function popUp(url){
	window.open(url,"popup","width=480,height=320");
}

function showPic(a){
	if(!document.getElementById("placeholder")){
		return false;
	}
	var src = a.href;
	var placeholder = document.getElementById("placeholder");
	placeholder.setAttribute("src",src);
	if(document.getElementById("description")){
		var title = a.getAttribute("title");
		var text = document.getElementById("description");
		text.firstChild.nodeValue = title;
	}
	return true;
}