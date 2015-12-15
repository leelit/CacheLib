addLoadEvent(positionMessage);
addLoadEvent(prepareSlideShow);


function prepareSlideShow(){
	moveTo("preview",0,0);
	var links = document.getElementsByTagName("li");
	links[0].onmouseover = function(){
		moveElement("preview",-100,0,10);
	}	
	links[1].onmouseover = function(){
		moveElement("preview",-200,0,10);
	}	
	links[2].onmouseover = function(){
		moveElement("preview",-300,0,10);
	}
}

function moveTo(id,destX,destY){
	var elem = document.getElementById(id);
	elem.style.position = "absolute";
	elem.style.left = destX+"px";
	elem.style.top = destY+"px";
}

function moveElement(id,destX,destY,interval){
	var elem = document.getElementById(id);
	var xpos = parseInt(elem.style.left);
	var ypos = parseInt(elem.style.top);
	if(elem.movement){
		clearTimeout(elem.movement);
	}
	if(xpos == destX && ypos == destY){
		return true;
	}
	if(xpos < destX){
		xpos++;
	}
	if(xpos > destX){
		xpos--;
	}
	if(ypos < destY){
		ypos++;
	}
	if(ypos > destY){
		ypos--;
	}
	elem.style.left = xpos + "px";
	elem.style.top = ypos + "px";
	var repeat = "moveElement('"+id+"',"+destX+","+destY+","+interval+")" 
	elem.movement = setTimeout(repeat,interval);
}

function positionMessage(){
	var elem = document.getElementById("message");
	elem.style.position = "absolute";
	if(!elem.style.left){
		elem.style.left = "0px";
	}
	if(!elem.style.top){
		elem.style.top = "0px";
	}
	moveElement("message",200,0,10);
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