(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .controller('AuteurDetailController', AuteurDetailController);

    AuteurDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Auteur'];

    function AuteurDetailController($scope, $rootScope, $stateParams, entity, Auteur) {
        var vm = this;
        vm.auteur = entity;
        vm.load = function (id) {
            Auteur.get({id: id}, function(result) {
                vm.auteur = result;
            });
        };
        var unsubscribe = $rootScope.$on('webstockerApp:auteurUpdate', function(event, result) {
            vm.auteur = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
