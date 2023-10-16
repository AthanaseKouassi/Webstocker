(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('LigneMissionActiviteDetailController', LigneMissionActiviteDetailController);

    LigneMissionActiviteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'LigneMissionActivite', 'Activite', 'Mission'];

    function LigneMissionActiviteDetailController($scope, $rootScope, $stateParams, entity, LigneMissionActivite, Activite, Mission) {
        var vm = this;
        vm.ligneMissionActivite = entity;
        vm.load = function (id) {
            LigneMissionActivite.get({id: id}, function(result) {
                vm.ligneMissionActivite = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:ligneMissionActiviteUpdate', function(event, result) {
            vm.ligneMissionActivite = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
