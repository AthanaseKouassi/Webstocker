(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('VilleDetailController', VilleDetailController);

    VilleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Ville', 'Region'];

    function VilleDetailController($scope, $rootScope, $stateParams, entity, Ville, Region) {
        var vm = this;
        vm.ville = entity;
        vm.load = function (id) {
            Ville.get({id: id}, function(result) {
                vm.ville = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:villeUpdate', function(event, result) {
            vm.ville = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
