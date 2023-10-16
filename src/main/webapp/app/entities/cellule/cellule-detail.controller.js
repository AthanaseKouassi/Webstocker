(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('CelluleDetailController', CelluleDetailController);

    CelluleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Cellule', 'User', 'Mission'];

    function CelluleDetailController($scope, $rootScope, $stateParams, entity, Cellule, User, Mission) {
        var vm = this;
        vm.cellule = entity;
        vm.load = function (id) {
            Cellule.get({id: id}, function(result) {
                vm.cellule = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:celluleUpdate', function(event, result) {
            vm.cellule = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
