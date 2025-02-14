/*
 * javascript functionality applicable to view_courtroom and create_courtroom pages
 * Author - harrism
 * Date - January 2024
 */
$(document).ready(function () {

	/* Page Initialization */
	btnHandler();

	/* Page Initialization end */

	/* Event Handlers */

	/* Handler for #selectCourt dropdown
	 * Clear messages, disable/enable buttons.
	 */ 
    $('#selectSite').change(function(e) {
		var elementId = $(this).prop('id');
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		clearMessages();
		btnHandler();
        loadCourtRooms();
		console.log(e.type + ' handler for element id : ' + elementId + " completes");
	});

	$('#selectCourtRoom').change(function(e) {
		var elementId = $(this).prop('id');
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		clearMessages();
		btnHandler();
        loadSelectedCourtRoom();
		console.log(e.type + ' handler for element id : ' + elementId + " completes");
	});

	$('#btnDelete').click(function(e) {		
		var elementId = $(this).prop('id');
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		clearMessages();
		updateBtnHandler();
		console.log(e.type + ' handler for element id : ' + elementId + " completes");
		return false;
	});

	/* Event Handlers end*/

	/* Page Specific Functions - Start */
    function loadCourtRooms() {
		console.log('loadCourtRooms starts...');
		if ($('#selectSite').find(':selected').size() && $('#selectSite').find(':selected').val() != '') {
			var id = $('#selectSite').find(':selected').val();
			var url = "/courtroom/delete_courtroom/"+id;
			console.log(url);
			$.getJSON(url)
				.done(function(json)
				{			    
					console.log('loadCourtRooms success');
					loadCourtRoomValues(json);
				})
				.fail(function(jqXHR, textStatus, error)
				{
					console.log('loadCourtRooms failure');
					console.log('error:'+error);
					resetCourtRoomValues();
				});
		} else {
			resetCourtRoomValues();
		}
		console.log('loadCourtRooms end');
	}

    function resetCourtRoomValues() {
		console.log("resetCourtRoomValues()");
        // Remove all options except the first
        $('#selectCourtRoom option:gt(0)').remove();
        resetSelectedCourtRoomValues();
	}

	function loadCourtRoomValues(json) {
        resetCourtRoomValues();
		console.log("selectCourtRoom="+json.options);
        $.each(json.options, function (i, item) {
            $('#selectCourtRoom').append($('<option>', { 
                value: item.value,
                text : item.text 
            }));
        });
	}

    function loadSelectedCourtRoom() {
		console.log('loadSelectedCourtRoom starts...');
		if ($('#selectCourtRoom').find(':selected').size() && $('#selectCourtRoom').find(':selected').val() != '') {
			var id = $('#selectCourtRoom').find(':selected').val();
			var url = "/courtroom/delete_courtroom/courtRoom/"+id;
			console.log(url);
			$.getJSON(url)
				.done(function(json)
				{			    
					console.log('loadSelectedCourtRoom success');
					loadSelectedCourtRoomValues(json);
				})
				.fail(function(jqXHR, textStatus, error)
				{
					console.log('loadSelectedCourtRoom failure');
					console.log('error:'+error);
					resetSelectedCourtRoomValues();
				});
		} else {
			resetSelectedCourtRoomValues();
		}
		console.log('loadSelectedCourtRoom end');
	}

    function resetSelectedCourtRoomValues() {
		console.log("resetSelectedCourtRoomValues()");
		$('#description').text('');
	}

    function loadSelectedCourtRoomValues(json) {
		console.log("courtRoom.description="+json.description);
		$('#description').text(json.description);
	}
	function btnHandler() {

		/* Enable/Disable buttons as required */
		console.log('btnHandler starts...');

		/* View Page*/
		if ($('#selectSite').size()) {
			$('#btnAdd').prop('disabled', true);
			$('#btnAmend').prop('disabled', true);
			$('#btnDelete').prop('disabled', true);

			// enable the buttons only if a court has been selected
			if ($('#selectCourtRoom').find(':selected').size() && $('#selectCourtRoom').find(':selected').val() != '') {
				$('#btnAdd').prop('disabled', false);
				$('#btnAmend').prop('disabled', false);
				$('#btnDelete').prop('disabled', false);
			}
		}		
		console.log('btnHandler ends');
	}

	function updateBtnHandler() {
		console.log('updateBtnHandler starts...');
		if ($('#originalNotification').val() != $('#notification').val())  {
			$('#notificationModal').modal('show');
		}
		else {
			$('#btnDeleteConfirm').click();	
		}		
		console.log('updateBtnHandler ends');		
	}
	
	function clearMessages() {
		setMessage('#messages_success', '');
		setMessage('#messages_error', '');
	}
	
	function setSuccessMessage(msg) {
		setMessage('#messages_success', msg);
		setMessage('#messages_error', '');
	}
	
	function setErrorMessage(msg) {
		setMessage('#messages_success','');
		setMessage('#messages_error', msg);
	}
	
	function setMessage(msgDiv, msg) {
	    $(msgDiv).empty();
	    if (msg == '') {
			$(msgDiv).hide();
		} else {
			$(msgDiv).show();
			$(document.createElement('p')).appendTo($(msgDiv)).text(msg);
		}
	}
	/* Page Specific Functions - End */

});
