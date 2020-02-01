jQuery(function($) {
	$(document).ready(function() {
		var socket = new WebSocket('ws://' + location.host + '/flights');
		//socket.addEventListener('message', function (message) {
		socket.onmessage = function(message) {
			console.log("msg- " + message.data);
			var flights = JSON.parse(message.data);
			var html = '<table border="0" cellspacing="0" cellpadding="5">'
				+ '<tr>'
				+ '<th align="left"><b>Image</th>'
				+ '<th align="left"><b>Flight</th>'
				+ '<th align="left"><b>From</th>'
				+ '<th align="left"><b>To</th>'
				+ '<th align="left"><b>Airline</th>'
				+ '<th align="left"><b>Aircraft</th>'
				+ '<th align="right"><b>Distance</th>'
				+ '</tr>';
			$.each(flights, function(index, flight) {
				html += '<tr>'
					+ '<td align="left"><img src="' + flight.image + '" width="50" height="25"></td>'
					+ '<td align="left">' + flight.number + '</td>'
					+ '<td align="left">' + flight.from + '</td>'
					+ '<td align="left">' + flight.to + '</td>'
					+ '<td align="left">' + flight.airline + '</td>'
					+ '<td align="left">' + flight.aircraft + '</td>'
					+ '<td align="right">' + flight.distance + '</td>'
					+ '</tr>';
			});
			html += '</table>';
			$('#out').html(html);
		//});
		};
	});
});