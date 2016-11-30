$(function() {
	$("#divPatDetailTabs")
			.tabs(
					{
						fxFade : true,
						fxSpeed : 'fast',
						beforeLoad : function(event, ui) {
							ui.jqXHR
									.error(function() {
										ui.panel
												.html("Couldn't load this tab. We'll try to fix this as soon as possible. "
														+ "If this wouldn't be a demo.");
									});
						}
					});
});