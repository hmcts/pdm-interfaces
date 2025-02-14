/*
 * javascript functionality applicable to view_judge amend_judge pages
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
	$('#selectJudge').change(function(e) {
		var elementId = $(this).prop('id');
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		clearMessages();
		btnHandler();
		loadJudge();
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
	function loadJudge() {
		console.log('loadJudge starts...');
		if ($('#selectJudge').find(':selected').size() && $('#selectJudge').find(':selected').val() != '') {
			var id = $('#selectJudge').find(':selected').val();
			var url = "/judge/amend_judge/"+id;
			console.log(url);
			$.getJSON(url)
				.done(function(json)
				{			    
					console.log('loadJudge success');
					loadJudgeValues(json);
				})
				.fail(function(jqXHR, textStatus, error)
				{
					console.log('loadJudge failure');
					console.log('error:'+error);
					resetJudgeValues();
				});
		} else {
			resetJudgeValues();
		}
		console.log('loadJudge end');
	}

	function resetJudgeValues() {
		console.log("resetJudgeValues()");
		$('#surname').val('');
		$('#firstName').val('');
		$('#middleName').val('');
		$('#title').val('');
		$('#fullListTitle1').val('');
		$('#selectJudgeType').val('');
	}

	function loadJudgeValues(json) {
		console.log("surname="+json.surname);
		$('#surname').val(json.surname);
		console.log("firstName="+json.firstName);
		$('#firstName').val(json.firstName);
		console.log("middleName="+json.middleName);
		$('#middleName').val(json.middleName);
		console.log("title="+json.title);
		$('#title').val(json.title);
		console.log("fullListTitle1="+json.fullListTitle1);
		$('#fullListTitle1').val(json.fullListTitle1);
		console.log("judgeType="+json.judgeType);
		$('#selectJudgeType').val(json.judgeType);
	}
	function btnHandler() {

		/* Enable/Disable buttons as required */
		console.log('btnHandler starts...');

		/* View Page*/
		if ($('#selectJudge').size()) {
			$('#btnAdd').prop('disabled', true);
			$('#btnAmend').prop('disabled', true);
			$('#btnDelete').prop('disabled', true);

			// enable the buttons only if a site has been selected
			if ($('#selectJudge').find(':selected').size() && $('#selectJudge').find(':selected').val() != '') {
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
