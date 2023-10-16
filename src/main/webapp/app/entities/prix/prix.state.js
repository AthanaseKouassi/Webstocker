(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prix', {
            parent: 'entity',
            url: '/prix',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.prix.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prix/prixes.html',
                    controller: 'PrixController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prix');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('prix-detail', {
            parent: 'prix',
            url: '/prix/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.prix.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prix/prix-detail.html',
                    controller: 'PrixDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prix');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Prix', function($stateParams, Prix) {
                    return Prix.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'prix',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('prix-detail.edit', {
            parent: 'prix-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prix/prix-dialog.html',
                    controller: 'PrixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prix', function(Prix) {
                            return Prix.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prix.new', {
            parent: 'prix',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prix/prix-dialog.html',
                    controller: 'PrixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateFixation: null,
                                actif: false,
                                prixUnitaire: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prix', null, { reload: 'prix' });
                }, function() {
                    $state.go('prix');
                });
            }]
        })
        .state('prix.edit', {
            parent: 'prix',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prix/prix-dialog.html',
                    controller: 'PrixDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prix', function(Prix) {
                            return Prix.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prix', null, { reload: 'prix' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prix.delete', {
            parent: 'prix',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prix/prix-delete-dialog.html',
                    controller: 'PrixDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Prix', function(Prix) {
                            return Prix.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prix', null, { reload: 'prix' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
