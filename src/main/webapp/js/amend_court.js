/*
 * javascript functionality applicable to view_court amend_court pages
 * Author - M.Harris
 * Date - January 2024
 */
$(document).ready(function () {

	/* Page Initialization */
	btnHandler();

	/* Page Initialization end */

	/* Event Handlers */

	/* Handler for #selectSite dropdown
	 * Clear messages, disable/enable buttons.
	 */ 
	$('#selectSite').change(function(e) {
		var elementId = $(this).prop('id');
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		clearMessages();
		btnHandler();
		loadCourtSite();
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
	function loadCourtSite() {
		console.log('loadCourtSite starts...');
		if ($('#selectSite').find(':selected').size() && $('#selectSite').find(':selected').val() != '') {
			var id = $('#selectSite').find(':selected').val();
			var url = "/court/amend_court/"+id;
			console.log(url);
			$.getJSON(url)
				.done(function(json)
				{			    
					console.log('loadCourtSite success');
					loadCourtSiteValues(json);
				})
				.fail(function(jqXHR, textStatus, error)
				{
					console.log('loadCourtSite failure');
					console.log('error:'+error);
					resetCourtSiteValues();
				});
		} else {
			resetCourtSiteValues();
		}
		console.log('loadCourtSite end');
	}

	function resetCourtSiteValues() {
		console.log("resetCourtSiteValues()");
		$('#courtSiteName').val('');
		$('#courtSiteCode').val('');
	}

	function loadCourtSiteValues(json) {
		console.log("CourtSiteName="+json.courtSiteName);
		$('#courtSiteName').val(json.courtSiteName);
		console.log("CourtSiteCode="+json.courtSiteCode);
		$('#courtSiteCode').val(json.courtSiteCode);
	}

	function btnHandler() {

		/* Enable/Disable buttons as required */
		console.log('btnHandler starts...');

		/* View Page*/
		if ($('#selectSite').size()) {
			$('#btnUpdateConfirm').prop('disabled', true);

			// enable the buttons only if a site has been selected
			if ($('#selectSite').find(':selected').size() && $('#selectSite').find(':selected').val() != '') {
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
