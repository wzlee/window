Ext.define('plat.store.cms.ServerAsyncStore', {
    extend: 'Ext.data.TreeStore',
    alias: 'widget.serverAsyncStore',
    model:'plat.model.cms.ServerAsyncModel',
    folderSort: true,
    autoLoad: false,
    autoSync: true,
    sorters: [{
        property: 'id',
        direction: 'ASC'
    }],
    proxy: {
        type: 'ajax',
        url: 'category/findCategoryParent',
        reader: {
            type: 'json',
            root: ''
        }
    }
});
