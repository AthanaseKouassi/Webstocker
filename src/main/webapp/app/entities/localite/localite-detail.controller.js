(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LocaliteDetailController', LocaliteDetailController);

    LocaliteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Localite', 'Magasin', 'Client', 'Mission', 'Commune'];

    function LocaliteDetailController($scope, $rootScope, $stateParams, entity, Localite, Magasin, Client, Mission, Commune) {
        var vm = this;
        vm.localite = entity;
        vm.load = function (id) {
            Localite.get({id: id}, function(result) {
                vm.localite = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:localiteUpdate', function(event, result) {
            vm.localite = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
