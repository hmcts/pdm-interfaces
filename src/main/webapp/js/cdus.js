/*
 * javascript functionality applicable to cdu page(s)
 * Author - M.Scullion
 * Date - December 2016
 */

$(document).ready(function () { 
	
	console.log('cdus.js script - starts');
	
	/* Page Initialization Start */
	btnHandler();
	
	/* Page Initialization End */
	
	/* Event Handlers Start */
	
	/* Handler for selectSite drop down
	 * Clear messages, mac address field and hide previous search results.
	 */ 
	$('#selectSite').change(function(e) {
		var elementId = $(this).prop('id');		
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		clearMessages();
		$('#macAddress').val('');
		$('#cduSearchResults').hide();
		$('#btnCduScreenshot').hide();
		console.log(e.type + ' handler for element id : ' + elementId + " completes");
	});
	
	/* Handler for mac address field
	 * Clear messages, selectSite drop down and hide previous search results.
	 */ 
	$('#macAddress').on('input', function(e) {
		var elementId = $(this).prop('id');		
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		clearMessages();
		$('#selectSite').val('');
		$('#cduSearchResults').hide();
		$('#btnCduScreenshot').hide();
		console.log(e.type + ' handler for element id : ' + elementId + " completes");
	});

	/* Handler for selectCdu drop down
	 * Enable / Disable cdu buttons dependant on data-registered and hide previous cdu info.
	 */
	$('#selectCdu').change(function(e) {
		var elementId = $(this).prop('id');		
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		btnHandler();
		$('#cduInformation').hide();
		$('#btnCduScreenshot').hide();
		console.log(e.type + ' handler for element id : ' + elementId + " completes");
	});
	
	/* Handler for selectUrl drop down
	 * Enable / Disable Remove Url Mapping button. 
	 */
	$('#selectUrl').change(function(e) {
		var elementId = $(this).prop('id');		
		console.log(e.type + ' handler for element id : ' + elementId + " starts");
		btnHandler();
		console.log(e.type + ' handler for element id : ' + elementId + " completes");
	});
	
	/* Handler for cdu screenshot button
	 * Open new window with screenshot or display error.
	 */ 
	$('#lnkScreenshot').click(function(e) {
		var elementId = $(this).prop('id');		
		console.log(e.type + ' handler for element id : ' + elementId + " starts");	

		var req = new XMLHttpRequest();
		req.open("GET", "/cdus/screenshot", true);
		req.responseType = "blob";
		req.onload = function(e) {
			if (req.status == 201) {
				var image = URL.createObjectURL(req.response);
				window.open(image);
				URL.revokeObjectURL(image);
				clearMessages();
			}
			else {
				console.log(e.type + ' handler for element id : ' + elementId + " status = " + req.status);	
				setErrorMessage("Error capturing CDU screenshot for " + $('#selectCdu').find(':selected').data('macaddress'));
			}
		};
		req.onerror = function(e) {
			console.log(e.type + ' handler for element id : ' + elementId + " status = " + req.status);	
			setErrorMessage("Error capturing CDU screenshot for " + $('#selectCdu').find(':selected').data('macaddress'));
		};
		req.onloadend = function(e) {
			$('#waitingModal').modal('hide');
		};
		
		$('#waitingModal').modal('show');
		req.send();

		console.log(e.type + ' handler for element id : ' + elementId + " completes");
		return false;
	});
	/* Event Handlers End */
	
	/* Page Specific Functions - Start */
	function btnHandler() {
		
		/* Enable/Disable buttons as required */
		console.log('btnHandler starts...');
		if ($('#selectUrl').size())
		{
			// Select box present....
			console.log('Working with url mapping related buttons');
			$('#btnRemoveMapping').prop('disabled', true);
			
			// If url selected in select box
			if ($('#selectUrl').find(':selected').size() && $('#selectUrl').find(':selected').val() != '')
			{
				$('#btnRemoveMapping').prop('disabled', false);
			}	
		}
		
		if ($('#selectCdu').size())
		{
			// Select box present....
			console.log('Working with cdu related buttons');
			
			// Initially disable all of the cdu buttons
			$('#btnRestartAllCdu').prop('disabled', true);
			$('#btnShowRegisterCdu').prop('disabled', true);
			$('#btnShowAmendCdu').prop('disabled', true);
			$('#btnUnRegisterCdu').prop('disabled', true);
			$('#btnRestartCdu').prop('disabled', true);
			$('#btnShowCdu').prop('disabled', true);
			$('#btnAddUrl').prop('disabled', true);
			$('#btnRemoveUrl').prop('disabled', true);
			
			// If cdu selected in select box
			if ($('#selectCdu').find(':selected').size() && $('#selectCdu').find(':selected').val() != '')
			{
				/* Grab the currently selected item and check the registration status */
				var registrationStatus = $('#selectCdu').find(':selected').data('registered');
				
				/* if we are displaying the select box w/o a selected mac address we won't have a data registered element !! */
				if (registrationStatus != undefined)
				{
					console.log('registration status : ' + registrationStatus);

					/* Show cdu information and restart regardless of registration status */
					$('#btnShowCdu').prop('disabled', false);
					$('#btnRestartCdu').prop('disabled', false);
					
					if (registrationStatus.toLowerCase() == 'n')
					{
						console.log('Enabling Registration button enabled.');
						$('#btnShowRegisterCdu').prop('disabled', false);
					}
					else if(registrationStatus.toLowerCase() == 'y')
					{
						console.log('Enabling buttons for registered cdus.');
						$('#btnShowAmendCdu').prop('disabled', false);
						$('#btnUnRegisterCdu').prop('disabled', false);
						$('#btnAddUrl').prop('disabled', false);
						$('#btnRemoveUrl').prop('disabled', false);
					}
				}			
			}
			
			// If search was by court site
			if ($('#selectSite').find(':selected').size() && $('#selectSite').find(':selected').val() != '' && $('#macAddress').val() == '')
			{
				// Show court site restart all cdus button
				$('#btnRestartAllCdu').prop('disabled', false);
			}
		}
		
		console.log('btnHandler ends');
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
	
	console.log('cdus.js script - ends');
});