angular
	.module('roomReservation')
	.controller('ReservationController',['$scope','$filter','ReservationDataOp',function($scope,$filter,ReservationDataOp){
		
		$scope.events =[];
		$scope.filteredEvents = [];
		
		$scope.rooms =[];
		$scope.uiConfig ={};
		
		ReservationDataOp
			.getEvents()
			.then(function(response){
				
				evt = response.data;
				
				evt.forEach(function(obj){
	    			
	    			$scope.events.push({
	    				id: obj.id,
	    				title: obj.title+' - '+ obj.room.room,
	    				start: new Date(obj.start),
	    				end: new Date(obj.end),
	    				color: obj.room.color_code,
	    				room_event: obj,
	    				editable: obj.createdBy.id == 1
	    			});
	    			
	    			
	    		});
			})
			.catch(function(error){
				console.log(error);
			})
		
	
		ReservationDataOp
			.getRooms()
			.then(function(response){
				$scope.rooms = response.data;
				
				$scope.rooms.splice(0,0,{id:0,room:'ALL'});
			}).
			catch(function(error){
				
			});

	    $scope.uiConfig.calendar = getCalendar($scope.events);
	    
		$scope.tabSelect = function(tab){
		
			events =(tab==0) ? $scope.events : $filter('filter')($scope.events,{room_event:{room:{id:tab}}},true);
			
			$scope.uiConfig.calendar = getCalendar(events);
		}
		
		
		/* alert on eventClick */
	    $scope.alertOnEventClick = function( date, jsEvent, view){
	        $scope.alertMessage = (date.title + ' was clicked ');
	        
	       // console.log(date);
	        
	        reservation ={
	        		id: date.id,
	        		title: date.title,
	        		start: $filter('date')(date.start.format('YYYY-MM-DD HH:mm:ss')),//moment.js format
	        		end: $filter('date')(date.end.format('YYYY-MM-DD HH:mm:ss')),
	        	}
	     
	        console.log(reservation);
	       
		};
		
		/* alert on Drop */
	    $scope.alertOnDrop = function(date, delta, revertFunc, jsEvent, ui, view){
	       
	       
	       s = date.start.format('YYYY-MM-DD HH:mm:ss');
	       e = date.end.format('YYYY-MM-DD HH:mm:ss');
	       
	       reservation = {
	    		   
	        		id: date.room_event.event.id,
	        		title: date.title,
	        		start: new Date(s),//moment.js format
	        		end: new Date(e),
	        	}
	     
	       
	       ReservationDataOp
		       	.updateEvent(reservation)
		       	.then(function(response){
		       		
		       		response.data.start = $filter('date')(response.data.start,'yyyy-MM-dd HH:mm:ss');
		       		response.data.end = $filter('date')(response.data.end,'yyyy-MM-dd HH:mm:ss');
		       		
		       		console.log(response);
		       	})
		       	.catch(function(error){
		       		console.log(error);
		       	})
	    };
	    
	    /* alert on Resize */
	    $scope
	      .alertOnResize = function(date, delta, revertFunc, jsEvent, ui, view ){
	    	 s = date.start.format('YYYY-MM-DD HH:mm:ss');
		       e = date.end.format('YYYY-MM-DD HH:mm:ss');
		       
		       reservation = {
		    		   
		        		id: date.room_event.event.id,
		        		title: date.title,
		        		start: new Date(s),//moment.js format
		        		end: new Date(e),
		        	}
		     
		       
		       ReservationDataOp
			       	.updateEvent(reservation)
			       	.then(function(response){
			       		
			       		response.data.start = $filter('date')(response.data.start,'yyyy-MM-dd HH:mm:ss');
			       		response.data.end = $filter('date')(response.data.end,'yyyy-MM-dd HH:mm:ss');
			       		
			       		console.log(response);
			       	})
			       	.catch(function(error){
			       		console.log(error);
			       	})
	    };
	    
	  
		 
		 function getCalendar(events){
		      
			//return a calendar object
			calendar = {
				        height: 450,
				        editable: true,
				        nextDayThreshold: "00:00:00",
				        timezone: false,
				        snapDuration: "00:05:00",
				        allDaySlot: false,
				        header:{
				        	left: 'prev,next today',
							center: 'title',
							right: 'month,agendaWeek,agendaDay,listMonth,listWeek,listDay'
				        },
				        views: {
				        	listMonth: { buttonText: 'list month' },
				        	listWeek: { buttonText: 'list week' },
				        	listDay: { buttonText: 'list day' },
				        },
				        eventClick: onEventClick,
				        eventDrop: onEventDrop,
				        eventResize: onResize,
				        events: events
			      };
			return calendar;
			
		 }
		 
		 
		 function onEventClick(date, jsEvent, view){
			  reservation ={
		        		id: date.id,
		        		title: date.title,
		        		start: $filter('date')(date.start.format('YYYY-MM-DD HH:mm:ss')),//moment.js format
		        		end: $filter('date')(date.end.format('YYYY-MM-DD HH:mm:ss')),
		        	}
		 }
		 
		 
		 function onEventDrop(date, delta, revertFunc, jsEvent, ui, view){
		       
		       
		       s = date.start.format('YYYY-MM-DD HH:mm:ss');
		       e = date.end.format('YYYY-MM-DD HH:mm:ss');
		       
		       reservation = {
		    		   
		        		id: date.room_event.id,
		        		title: date.title,
		        		start: new Date(s),//moment.js format
		        		end: new Date(e),
		        		room: date.room_event.room,
		        		createdBy: date.room_event.createdBy
		        	}
		     
		       console.log(reservation);
		       
		       
//		       ReservationDataOp
//		       	.getConflicts(reservation)
//		       	.then(function(response){
//		       		console.log(response);
//		       	})
//		       	.catch(function(error){
//		       		console.log(error);
//		       	})
		       
		       
//		       ReservationDataOp
//			    .updateEvent(reservation)
//			    .then(function(response){
//			       		
//		       		response.data.start = $filter('date')(response.data.start,'yyyy-MM-dd HH:mm:ss');
//		       		response.data.end = $filter('date')(response.data.end,'yyyy-MM-dd HH:mm:ss');
//		       		
//		       		console.log(response);
//		       	})
//		       	.catch(function(error){
//		       		console.log(error);
//		       		revertFunc();
//		       	})
		 }
		 
		 function onResize(date, delta, revertFunc, jsEvent, ui, view ){
			   s = date.start.format('YYYY-MM-DD HH:mm:ss');
		       e = date.end.format('YYYY-MM-DD HH:mm:ss');
		       
		       reservation = {
		    		   
		        		id: date.room_event.id,
		        		title: date.title,
		        		start: new Date(s),//moment.js format
		        		end: new Date(e),
		        		room: date.room_event.room,
		        		createdBy: date.room_event.createdBy
		        	}
		     
		       console.log(reservation);
		       
		       
		       ReservationDataOp
			    .updateEvent(reservation)
			    .then(function(response){
			       		
		       		response.data.start = $filter('date')(response.data.start,'yyyy-MM-dd HH:mm:ss');
		       		response.data.end = $filter('date')(response.data.end,'yyyy-MM-dd HH:mm:ss');
		       		
		       		console.log(response);
		       	})
		       	.catch(function(error){
		       		console.log(error);
		       		revertFunc();
		       	})
		 }
		 
		
		 
	}]);