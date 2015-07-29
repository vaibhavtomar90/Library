var uiModels = uiModels || {};

uiModels.locationtypes = {
    id: 'locationtypes',
    label: 'Location Types',
    entity: 'location types',
    entities: 'location types',
    leadfield:'name',
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
                    id: 'name', attribute: 'name', type: 'text', label: 'Name', required: true, 
                    maxlength: 255,
                    width: 62, viewmany: true
                }
            ]
        }
    ]
};
