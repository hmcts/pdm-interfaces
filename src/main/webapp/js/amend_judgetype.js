/*
 * javascript functionality applicable to view_judgetype amend_judgetype pages
 * Author - N.Toft
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
	$('#selectCode').change(function(e) {
		var elementId = $(this).prop('id');
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		clearMessages();
		btnHandler();
		loadJudgeType();
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
	function loadJudgeType() {
		console.log('loadJudgeType starts...');
		if ($('#selectCode').find(':selected').size() && $('#selectCode').find(':selected').val() != '') {
			var id = $('#selectCode').find(':selected').val();
			var url = "/judgetype/amend_judgetype/"+id;
			console.log(url);
			$.getJSON(url)
				.done(function(json)
				{			    
					console.log('loadJudgeType success');
					loadJudgeTypeValues(json);
				})
				.fail(function(jqXHR, textStatus, error)
				{
					console.log('loadJudgeType failure');
					console.log(error);
					resetJudgeTypeValues();
				});
		} else {
			resetJudgeTypeValues();
		}
		console.log('loadJudgeType end');
	}

	function resetJudgeTypeValues() {
		console.log("resetJudgeTypeValues()");
		$('#description').val('');
	}

	function loadJudgeTypeValues(json) {
		console.log("description="+json.deCode);
		$('#description').val(json.deCode);
	}
	function btnHandler() {

		/* Enable/Disable buttons as required */
		console.log('btnHandler starts...');

		/* View Page*/
		if ($('#selectCode').size()) {
			$('#btnAdd').prop('disabled', true);
			$('#btnAmend').prop('disabled', true);

			// enable the buttons only if a site has been selected
			if ($('#selectCode').find(':selected').size() && $('#selectCode').find(':selected').val() != '') {
				$('#btnAdd').prop('disabled', false);
				$('#btnAmend').prop('disabled', false);
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
