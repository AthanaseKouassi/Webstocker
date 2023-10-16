(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('TypeActiviteDetailController', TypeActiviteDetailController);

    TypeActiviteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'TypeActivite', 'Activite'];

    function TypeActiviteDetailController($scope, $rootScope, $stateParams, entity, TypeActivite, Activite) {
        var vm = this;
        vm.typeActivite = entity;
        vm.load = function (id) {
            TypeActivite.get({id: id}, function(result) {
                vm.typeActivite = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:typeActiviteUpdate', function(event, result) {
            vm.typeActivite = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
