(function() {
    'use strict';

    angular
        .module('webstockerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('type-activite', {
            parent: 'entity',
            url: '/type-activite',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.typeActivite.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-activite/type-activites.html',
                    controller: 'TypeActiviteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeActivite');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('type-activite-detail', {
            parent: 'entity',
            url: '/type-activite/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'webstockerApp.typeActivite.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/type-activite/type-activite-detail.html',
                    controller: 'TypeActiviteDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('typeActivite');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TypeActivite', function($stateParams, TypeActivite) {
                    return TypeActivite.get({id : $stateParams.id});
                }]
            }
        })
        .state('type-activite.new', {
            parent: 'type-activite',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-activite/type-activite-dialog.html',
                    controller: 'TypeActiviteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                libelle: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('type-activite', null, { reload: true });
                }, function() {
                    $state.go('type-activite');
                });
            }]
        })
        .state('type-activite.edit', {
            parent: 'type-activite',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-activite/type-activite-dialog.html',
                    controller: 'TypeActiviteDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TypeActivite', function(TypeActivite) {
                            return TypeActivite.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-activite', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('type-activite.delete', {
            parent: 'type-activite',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/type-activite/type-activite-delete-dialog.html',
                    controller: 'TypeActiviteDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TypeActivite', function(TypeActivite) {
                            return TypeActivite.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('type-activite', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
