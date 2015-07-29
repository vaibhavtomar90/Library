var uiModels = uiModels || {};

uiModels.locations = {
    id: 'locations',
    label: 'Locations',
    entity: 'locations',
    entities: 'locations',
    //icon: 'serie.gif',
    leadfield:'title',
    elements: [
        {
            type: 'panel', label: 'Basic Data', width: 70,
            elements: [
                {
                    id: 'code', attribute: 'code', type: 'text', label: 'Code', required: true, 
                    maxlength: 255,
                    width: 62, viewmany: true
                },
                {
                    id: 'title', attribute: 'title', type: 'text', label: 'Title', required: true, 
                    maxlength: 255,
                    width: 62, viewmany: true
                },
                {
                    id: 'type', attribute: 'type', type: 'lov', label: 'Type', width: 38, viewmany: true,
                    list: [
                        {id: 'country', text: 'Country'},
                        {id: 'state', text: 'State'},
                        {id: 'city', text: 'City'},
                        {id: 'pincode', text: 'Pincode'}
                    ]
                }
            ]
        }
    ]
};
