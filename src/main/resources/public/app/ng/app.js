angular
 .module('roomReservation', ['ui.router','ui.calendar', 'ui.bootstrap','ui.bootstrap.datetimepicker']);

angular
 .module('roomReservation')
 .config(['$stateProvider', '$urlRouterProvider', '$locationProvider',function($stateProvider, $urlRouterProvider, $locationProvider) {
    $urlRouterProvider.otherwise('/');
    	  
    $stateProvider	
    
	    .state("home", {
	        url: '/',
	        templateUrl: '/RoomReservation/app/partials/dashboard.html', 
	        controller: 'DashboardController',       
	    })
    	 .state("rooms", {
	        url: '/rooms',
	        templateUrl: '/RoomReservation/app/partials/room.html', 
	        controller: 'RoomController',
    	 })
    	 .state("events", {
	        url: '/events',
	        templateUrl: '/RoomReservation/app/partials/events.html', 
	        controller: 'EventController',
    	 })
	    .state("login", {
	        url: '/login',
	        templateUrl: '/RoomReservation/app/partials/login.html', 
	        controller: 'LoginController',
    	 })
  }]);