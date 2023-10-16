(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneprixproduitDetailController', LigneprixproduitDetailController);

    LigneprixproduitDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Ligneprixproduit'];

    function LigneprixproduitDetailController($scope, $rootScope, $stateParams, entity, Ligneprixproduit) {
        var vm = this;
        vm.ligneprixproduit = entity;
        vm.load = function (id) {
            Ligneprixproduit.get({id: id}, function(result) {
                vm.ligneprixproduit = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:ligneprixproduitUpdate', function(event, result) {
            vm.ligneprixproduit = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
