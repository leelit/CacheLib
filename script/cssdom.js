addLoadEvent(function (){
	styleHeaderSilbings("h1","intro")
});
addLoadEvent(styleTables);

function styleHeaderSilbings(tag,className){
	var headers = document.getElementsByTagName(tag);
	var elem;
	for(var i = 0; i < headers.length; i++){
		elem = getNextElement(headers[i].nextSibling);
		addClass(elem,className);
	}
}

function addClass(element,value){
	if(!element.className){
		element.className = value;
	}else{
		var origin = element.className;
		element.className = origin+ " " +value;
	}
}

function styleTables(){
	var tables = document.getElementsByTagName("table");
	var rows = tables[0].getElementsByTagName("tr");
	for(var i = 0; i < rows.length; i++){
		if(i % 2 != 0){
			rows[i].style.backgroundColor = "#ffc";	
		}
		rows[i].onmouseover = function(){
			this.style.fontWeight = "bold";
		}
		rows[i].onmouseout = function(){
			this.style.fontWeight = "normal";
		}
	}
}

function getNextElement(node){
	if(node.nodeType == 1){
		return node;
	}
	if(node.nextSibling){
		return getNextElement(node.nextSibling);
	}
	return null;
}

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