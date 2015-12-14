addLoadEvent(prepareLinks);
addLoadEvent(prepareGallery);
addLoadEvent(displayLinks);
addLoadEvent(testInsertAfter)

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

function testInsertAfter(){
	var gallery = document.getElementById("gallery");
	var h1 = document.createElement("h2");
	var h1_text = document.createTextNode("insertBefore or after");
	h1.appendChild(h1_text);
	// h2_dom.parentNode.insertBefore(h1,gallery);
	insertAfter(h1,gallery);
}

function insertAfter(newElement,targetElement){
	var parent = targetElement.parentNode;
	if(parent.lastChild == targetElement){
		parent.appendChild(newElement);
	}else{
		parent.insertBefore(newElement,targetElement.nextSibling);
	}
}


function displayLinks(){
	var a = document.getElementById("gallery").getElementsByTagName("a");
	if(a.length < 1) return false;
	var hrefs = new Array();
	for(var i = 0; i < a.length; i++){
		var title = a[i].getAttribute("title");
		var path = a[i].getAttribute("href");
		hrefs[title] = path;
	}
	var ol = document.createElement("ol");
	for(title in hrefs){
		var path = hrefs[title];
		var li = document.createElement("li");
		var text_node = title+":  "+path;
		var text = document.createTextNode(text_node);
		li.appendChild(text);
		ol.appendChild(li);
	}
	var h2 = document.createElement("h2");
	var h2_text = document.createTextNode("ImagesPath");
	h2.appendChild(h2_text);
	document.body.appendChild(h2);
	document.body.appendChild(ol);
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
	// 健壮性代码...
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