angular
	.module('roomReservation')
	.controller('NavController',['$rootScope', '$scope', '$state','Access',
		function($rootScope, $scope, $state,Access){
		
			$scope.isLoggedIn = function(){
				//return Access.isAuthenticated();
				return true;
			}
		
	}]); 