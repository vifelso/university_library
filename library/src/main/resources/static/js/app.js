function makeOrder(bookid) {
	$("#" + bookid).load('/placeorder?bookid=' + bookid,
			function(responseTxt, statusTxt, xhr) {
				if (statusTxt == "success")
					this.innerHTML = "Booked";
			});
}

function cancelOrder(bookid) {
	$("#book-orders").load('/cancelorder?bookid=' + bookid);
}