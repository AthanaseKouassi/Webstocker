(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LignefactureDetailController', LignefactureDetailController);

    LignefactureDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Lignefacture', 'Lot', 'Facture'];

    function LignefactureDetailController($scope, $rootScope, $stateParams, entity, Lignefacture, Lot, Facture) {
        var vm = this;
        vm.lignefacture = entity;
        vm.load = function (id) {
            Lignefacture.get({id: id}, function(result) {
                vm.lignefacture = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:lignefactureUpdate', function(event, result) {
            vm.lignefacture = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
