/*
 * javascript functionality applicable to view_hearing amend_hearing pages
 * Author - L.Gittins
 * Date - December 2023
 */
$(document).ready(function () {

	/* Page Initialization */
	btnHandler();

	/* Page Initialization end */

	/* Event Handlers */

	/* Handler for #selectSite dropdown
	 * Clear messages, disable/enable buttons.
	 */
	$('#selectHearingType').change(function(e) {
		var elementId = $(this).prop('id');
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		clearMessages();
		btnHandler();
		loadHearingType();
		console.log(e.type + ' handler for element id : ' + elementId + " completes");
	});

	$('#btnUpdate').click(function(e) {
		var elementId = $(this).prop('id');
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		clearMessages();
		updateBtnHandler();
		console.log(e.type + ' handler for element id : ' + elementId + " completes");
		return false;
	});

	/* Event Handlers end*/

	/* Page Specific Functions - Start */
    function loadHearingType() {
		console.log('loadHearingType starts...');
		if ($('#selectHearingType').find(':selected').size() && $('#selectHearingType').find(':selected').val() != '') {
			var id = $('#selectHearingType').find(':selected').val();
			var url = "/hearing/amend_hearing/"+id;
			console.log(url);
			$.getJSON(url)
				.done(function(json)
				{
					console.log('loadHearingType success');
					loadHearingTypeValues(json);
				})
				.fail(function(jqXHR, textStatus, error)
				{
					console.log('loadHearingType failure');
					console.log('error:'+error);
					resetHearingTypeValues();
				});
		} else {
			resetHearingTypeValues();
		}
		console.log('loadHearingType end');
	}

	function resetHearingTypeValues() {
		console.log("resetHearingValues()");
		$('#selectHearingType').val('');
		$('#hearingTypeCode').val('');
		$('#hearingTypeDesc').val('');
		$('#category').val('');
	}

	function loadHearingTypeValues(json) {
		console.log("refHearingTypeId="+json.refHearingTypeId);
		$('#selectHearingType').val(json.refHearingTypeId);
		console.log("hearingTypeCode="+json.hearingTypeCode);
		$('#hearingTypeCode').val(json.hearingTypeCode);
		console.log("hearingTypeDesc="+json.hearingTypeDesc);
		$('#hearingTypeDesc').val(json.hearingTypeDesc);
		console.log("category="+json.category);
		$('#category').val(json.category);
	}

	function btnHandler() {
		/* Enable/Disable buttons as required */
		console.log('btnHandler starts...');
		/* Amend Page*/
		if ($('#selectHearingType').size()) {
			$('#btnUpdateConfirm').prop('disabled', true);
			// enable the buttons only if a site has been selected
			if ($('#selectHearingType').find(':selected').size() && $('#selectHearingType').find(':selected').val() != '') {
				$('#btnUpdateConfirm').prop('disabled', false);
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
			$('#btnUpdateConfirm').click();
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
