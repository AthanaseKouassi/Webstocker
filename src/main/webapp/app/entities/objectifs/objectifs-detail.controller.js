(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('ObjectifsDetailController', ObjectifsDetailController);

    ObjectifsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Objectifs', 'Produit'];

    function ObjectifsDetailController($scope, $rootScope, $stateParams, entity, Objectifs, Produit) {
        var vm = this;
        vm.objectifs = entity;
        vm.load = function (id) {
            Objectifs.get({id: id}, function(result) {
                vm.objectifs = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:objectifsUpdate', function(event, result) {
            vm.objectifs = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
