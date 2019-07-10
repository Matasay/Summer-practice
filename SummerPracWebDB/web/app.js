
Vue.component('tab-grid', {
    template: '#tab-grid-template',
    props: {
        heroes: Array,
        columns: Array,
        filterKey: String
    },
    data: function () {
        var sortOrders = {}
        this.columns.forEach(function (key) {
            sortOrders[key] = 1
        })
        return {
            sortKey: '',
            sortOrders: sortOrders
        }
    },
    computed: {
        filteredHeroes: function () {
            var sortKey = this.sortKey
            var filterKey = this.filterKey && this.filterKey.toLowerCase()
            var order = this.sortOrders[sortKey] || 1
            var heroes = this.heroes
            if (filterKey) {
                heroes = heroes.filter(function (row) {
                    return Object.keys(row).some(function (key) {
                        return String(row[key]).toLowerCase().indexOf(filterKey) > -1
                    })
                })
            }
            if (sortKey) {
                heroes = heroes.slice().sort(function (a, b) {
                    a = a[sortKey]
                    b = b[sortKey]
                    return (a === b ? 0 : a > b ? 1 : -1) * order
                })
            }
            return heroes
        }
    },
    filters: {
        capitalize: function (str) {
            return str.charAt(0).toUpperCase() + str.slice(1)
        }
    },
    methods: {
        sortBy: function (key) {
            this.sortKey = key
            this.sortOrders[key] = this.sortOrders[key] * -1
        }
    }
})

new Vue({
    el: '#app',
    data: {
        myXRSFToken: 'e4edcb6c-bd1c-46c1-863c-421235c522a3',
        searchQuery: '',
        gridColumns:[],
        gridData: []
        },
    methods: {
        printAccounts: function (event) {
            axios.get('http://localhost:8080/app_software_store', {
                credentials: 'include',
                headers: {
                    "Content-Type": "application/json",
                    "Accept": "application/json",
                    "x-xsrf-token": myXRSFToken
                },
                params: {
                    table: 'accounts',
                    operation: 'view'
                }
            })
            .then((response) => {
                searchQuery = '';
                gridColumns = ['email','password_cpt', 'first_name', 'last_name', 'date_birth', 'gender', 'avatar_name' ];
                for (var i = 0; i < response.data.length; i++) {
                                 this.gridData.push({
                                     email: response.data[i].email,
                                     password_cpt: response.data[i].password_cpt,
                                     first_name: response.data[i].first_name,
                                     last_name: response.data[i].last_name,
                                    date_birth: response.data[i].date_birth,
                                     gender: response.data[i].gender,
                                     avatar_name: response.data[i].avatar_name
                                 })
                             }
            })}
        ,

        //for (var i=0; i < response.data.length; i++){
        //                 this.entries.push({
        //                     email: response.data[i].email,
        //                     password_cpt: response.data[i].password_cpt,
        //                     first_name: response.data[i].first_name,
        //                     last_name: response.data[i].last_name,
        //                     date_birth: response.data[i].date_birth,
        //                     gender: response.data[i].gender,
        //                     avatar_name: response.data[i].avatar_name
        //                 })
        //             }

        printPurchases: function (event) {
            alert("HDNW")
        },
        printPurchasedItems: function (event) {

        }
    }
})