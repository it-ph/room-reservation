angular
	.module("roomReservation")
	.factory('ReservationDataOp',['$http',function($http){
		
		var ReservationDataOp ={};
		
		ReservationDataOp
			.getReservations = function(){
			
			return $http({
				url: '/RoomReservation/reservations',
				method: 'GET',
			});
		}
		
		ReservationDataOp
			.getRoomEvents = function(){
			
			return $http({
				url: '/RoomReservation/room-events',
				method: 'GET',
			});
		}
		
		ReservationDataOp
			.getEvents = function(){
			
			return $http({
				url: '/RoomReservation/events',
				method: 'GET',
			});
		}
	
		ReservationDataOp
			.updateEvent = function(event){
			
			return $http({
				url: '/RoomReservation/events',
				method: 'PUT',
				data: event,
				headers: { 'Content-Type': 'application/json; charset=UTF-8' },
			});
		}
		
		ReservationDataOp
			.addEvent = function(event){
			
			return $http({
				url: '/RoomReservation/events',
				method: 'POST',
				data: event,
				headers: { 'Content-Type': 'application/json; charset=UTF-8' },
			});
		}
		
		ReservationDataOp
			.addEvent = function(event){
			
			return $http({
				url: '/RoomReservation/events/newEvents',
				method: 'POST',
				data: event,
				headers: { 'Content-Type': 'application/json; charset=UTF-8' },
			});
		}
		ReservationDataOp
			.getConflicts = function(reservation){
			//console.log(reservation);
			return $http({
				url: '/RoomReservation/events/getConflicts',
				method: 'POST',
				dataType: 'json',
				data: reservation,
				headers: { 'Content-Type': 'application/json; charset=UTF-8' }
			})
		}
		
		ReservationDataOp
			.getRooms = function(){
			return $http({
				url: '/RoomReservation/rooms',
				method: 'GET'
			})
		}
		
		ReservationDataOp
		  .getEventsByRoom = function(id){
			return $http({
				url: '/RoomReservation/room-events/room/'+id,
				method: 'GET'
			})
		}
		return ReservationDataOp;
	}]);