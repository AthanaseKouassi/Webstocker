(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('MissionDetailController', MissionDetailController);

    MissionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Mission', 'LigneMissionActivite', 'LigneBudget', 'Localite', 'Cellule'];

    function MissionDetailController($scope, $rootScope, $stateParams, entity, Mission, LigneMissionActivite, LigneBudget, Localite, Cellule) {
        var vm = this;
        vm.mission = entity;
        vm.load = function (id) {
            Mission.get({id: id}, function(result) {
                vm.mission = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:missionUpdate', function(event, result) {
            vm.mission = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
