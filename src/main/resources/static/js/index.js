$('#request-button').click(function () {
	setMessage('')
	inputCode = $('#code').val()
	if (inputCode === '') return;

	if (!validateCode(inputCode))
	{
		setMessage('Invalid code format.')
		return
	}

	$.ajax({
		type: 'GET',
		url: '/api/callingcode/' + inputCode,
		success: onFindCodeSuccess,
		error: function () {
			setMessage('An error has occurred');
		}
	});
});

function onFindCodeSuccess(data)
{
	countryData = JSON.parse(data)
	setMessage(countryData.ok ? `${countryData.country} (${countryData.timezone})` : countryData.error)
}

function setMessage(msg)
{
	$('#message').html('<p>' + msg + '</p>')
}

function validateCode(code)
{
	var reg = /^(\+?)(\d(\s?))+$/
	return reg.test(code)
}