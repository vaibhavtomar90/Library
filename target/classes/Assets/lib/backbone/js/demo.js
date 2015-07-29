function fetchDataFromRemoteServer(entityName, collections){
    
    var entity = Backbone.Model.extend({
        urlRoot: '/masterdata/' + entityName
    });

    var entityCollection = Backbone.Collection.extend({
        url: "/masterdata/" + entityName,
        model: entity,
        initialize: function(){
        	this.fetch({
            	success: this.fetchSuccess,
            	async: false,
            	error: this.fetchError
        	});
    	},
    	fetchSuccess: function (collection, response) {
        	console.log('Collection fetch success', response);
        	console.log('Collection models: ', collection.models);
    	},
		fetchError: function (collection, response) {
        	throw new Error("Books fetch error");
    	}	
    });
    
    var currentEntityCollection = new entityCollection();
    
    collections[entityName] = currentEntityCollection;

}

function showUIModel(uiModel){
    if(_.isString(uiModel)){
        console.log(uiModels);
        uiModel=uiModels[uiModel];
    }
    $('#uimodel').html(Evol.UI.input.textMJSON('uimodel2', uiModel, 10, true))
        .slideDown();
    $('#hide_def').show();
}

function hideUIModel(){
    $('#uimodel').slideUp();
    $('#hide_def').hide();
}
