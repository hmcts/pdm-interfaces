/*
 * javascript functionality applicable to view_display amend_display pages
 * Author - M.Harris
 * Date - December 2023
 */
$(document).ready(function () {

	/* Page Initialization */

	/* Page Initialization end */

	/* Event Handlers */

	/* Handler for #selectSite dropdown
	 * Clear messages, disable/enable buttons.
	 */ 
	$('#selectDisplay').change(function(e) {
		var elementId = $(this).prop('id');
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		clearMessages();
		loadDisplay();
		console.log(e.type + ' handler for element id : ' + elementId + " completes");
	});

	$('#selectCourtSite').change(function(e) {
		var elementId = $(this).prop('id');
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		clearMessages();
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
	function loadDisplay() {
		console.log('loadDisplay starts...');
		if ($('#selectDisplay').find(':selected').size() && $('#selectDisplay').find(':selected').val() != '') {
			var id = $('#selectDisplay').find(':selected').val();
			var url = "/display/amend_display/"+id;
			console.log(url);
			$.getJSON(url)
				.done(function(json)
				{			    
					console.log('loadDisplay success');
					loadDisplayValues(json);
				})
				.fail(function(jqXHR, textStatus, error)
				{
					console.log('loadDisplay failure');
					console.log('error:'+error);
					resetDisplayValues();
				});
		} else {
			resetDisplayValues();
		}
		console.log('loadDisplay end');
	}

	function resetDisplayValues() {
		console.log("resetDisplayValues()");
		$('#selectDisplayType').val('');
		$('#selectRotationSet').val('');
	}

	function loadDisplayValues(json) {
		clearMessages();
		console.log("displayTypeId="+json.displayTypeId);
		$('#selectDisplayType').val(json.displayTypeId);
		console.log("xhibitCourtSiteId="+json.courtSite.id);
		$('#selectCourtSite').val(json.courtSite.id);
		console.log("rotationSetId="+json.rotationSetId);
		$('#selectRotationSet').val(json.rotationSetId);
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
