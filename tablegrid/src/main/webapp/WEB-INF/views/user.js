$(function() {
	 $.ajax({
         url: 'find/all',        
         method: 'GET'
     }).done(function(data) {
    	 var $userTmpl = _.template($('#user-tpl').html());
         $('.user-list').html($userTmpl({
             data: data
         }));
     }).fail(function() {
         
     });
});
