
function download(name, blob) {
	var href = window.URL.createObjectURL(blob);
	var link = document.createElement('a');

	link.download = name;
	link.href = href;

	document.body.appendChild(link);
	
	link.click();    
	link.remove();   
}

function show_output(id, content) {
	elem(id).innerHTML = content;
	
	elem(id).className = "output code";
}
async function get(url) {
	var response = await fetch(url, { method: 'get' });

	if (!response.ok) throw new Error('Get request returned an error.');

	return response;
}

async function get_json(url) {
	var response = await get(url);

    return await response.json();
}

async function get_file(url) {
	var response = await get(url);
    var blob = await response.blob();
    var name = url.slice(url.lastIndexOf("/") + 1);

    return new File([blob], name);
}

async function post(url, data) {
	var response = await fetch(url, { method: 'post', body: data });

	if (!response.ok) throw new Error('Post request returned an error.');

	return response;
}

function read_files(id, data, name, mandatory) {
	var files = Array.from(elem(id).files);
	
	if (files.length == 0 && mandatory) throw new Error("Parameter " + name + " is mandatory.");
	
	files.forEach(f => data.append(name, f)) ;
}

function read_value(id, data, name, mandatory) {
	var value = elem(id).value;

	if (value.length == 0 && mandatory) throw new Error("Parameter " + name + " is mandatory.");
	
	data.append(name, value)
}

function elem(id) {
	return document.querySelector("#" + id);
}