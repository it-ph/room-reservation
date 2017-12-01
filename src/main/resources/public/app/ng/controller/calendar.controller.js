angular
	.module('roomReservation')
	.controller('CalendarController',['$scope','$filter','ReservationDataOp',function($scope,$filter,ReservationDataOp){
		
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
	    				id: (obj.repeatId == 0) ? undefined: obj.repeatId,
	    				title: obj.title+' - '+ obj.room.room,
	    				start: moment.utc(obj.start).format(),
	    				end: moment.utc(obj.end).format(),
	    				color: obj.room.color_code,
	    				room_event: obj,
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
	    $scope.alertOnResize = function(date, delta, revertFunc, jsEvent, ui, view ){
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
	    
	   $scope.conflictEvents =[];
	   $scope.numConflicts = 0;
		 
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
			  console.log(reservation);
		 }
		 
		 
		 function onEventDrop(date, delta, revertFunc, jsEvent, ui, view){
		       	       
		       s = moment(date.start);
		       e = moment(date.end);
		       
		       $scope.conflictEvents =[];
		       
		       events = [];
		       droppedEvents =[];
		       
		       
		       if(date.id ==undefined){ //if no repeat event was dropped
		    	   
		    	   
		    	   events.push({
		    		   
		        		id: date.room_event.id,
		        		title: date.room_event.title,
		        		start: new Date(s),
		        		end: new Date(e),
		        		room: date.room_event.room,
		        		createdBy: date.room_event.createdBy
		        	});
		    	   
		    	   droppedEvents = events;
		    	   
		       }else{ //if repeating events was dropped
		    	   
		    	   events = view.calendar.clientEvents(date.id);
		    	   
		    	   events.forEach(function(o){
		    		   
		    		   st = moment(o.start);
		    		   en = moment(o.end);
		    		   
		    		   droppedEvents.push({
		    				id: o.room_event.id,
		    				repeatId: o.room_event.repeatId,
			        		title: date.room_event.title,
			        		start: new Date(st),//moment.js format
			        		end: new Date(en),
			        		room: o.room_event.room,
			        		createdBy: o.room_event.createdBy
		    			});
		    		   
		    	   });
		    	   
		    	   
		    	   
		       }
		       console.log(droppedEvents);
		       var revert = false;
		       
	    	   var numConflict = 0;
		       
	    	   ReservationDataOp
			    .getConflicts(droppedEvents)
			    .then(function(response){
			       		
		       		
		       		evt = response.data;
		       		
	       			evt.forEach(function(obj){
						
						ev = {
								id: (obj.repeatId == 0) ? undefined: obj.repeatId,
			    				title: obj.title+' - '+ obj.room.room,
			    				start: new Date(moment(obj.start).format('YYYY-MM-DD HH:mm:ss')),
			    				end: new Date(moment(obj.end).format('YYYY-MM-DD HH:mm:ss')),
			    				color: obj.room.color_code,
			    				room_event: obj,
				    				
				    		};
		    			$scope.conflictEvents.push(ev);

		    		});
		       		
	       			if(response.data.length> 0){
	       				revertFunc();
	       				console.log(evt);
	       			}else{
	       				
	       			  ReservationDataOp
	       			   .updateEvent(droppedEvents)
	       			   .then(function(response){
	       				  //console.log(response); 
	       			   })
	       			   .catch(function (error){
	       				   console.log(error);
	       			   });
	       			}
					
		       		
		       		
		       	})
		       	.catch(function(error){
		       		console.log(error);
		       	});
	    	   
	    	   
		 }
		 
		 function onResize(date, delta, revertFunc, jsEvent, ui, view ){
//			   s = date.start.format('YYYY-MM-DD HH:mm:ss');
//		       e = date.end.format('YYYY-MM-DD HH:mm:ss');
//		       
//		       reservation = {
//		    		   
//		        		id: date.room_event.id,
//		        		title: date.title,
//		        		start: new Date(s),//moment.js format
//		        		end: new Date(e),
//		        		room: date.room_event.room,
//		        		createdBy: date.room_event.createdBy
//		        	}
//		     
//		       console.log(reservation);
//		       
//		       
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
			 
			   s = moment(date.start);
		       e = moment(date.end);
		       
		       $scope.conflictEvents =[];
		       
		       events = [];
		       droppedEvents =[];
		       
		       console.log(s);
		       console.log(e);
		       
			 if(date.id ==undefined){ //if no repeat event was dropped
		    	   
		    	   
		    	   events.push({
		    		   
		        		id: date.room_event.id,
		        		title: date.room_event.title,
		        		start: new Date(s),
		        		end: new Date(e),
		        		room: date.room_event.room,
		        		createdBy: date.room_event.createdBy
		        	});
		    	   
		    	   droppedEvents = events;
		    	   console.log('single');
		       }else{ //if repeating events was dropped
		    	   
		    	   events = view.calendar.clientEvents(date.id);
		    	   
		    	   events.forEach(function(o){
		    		   
		    		   st = moment(o.start);
		    		   en = moment(o.end);
		    		   
		    		   droppedEvents.push({
		    				id: o.room_event.id,
		    				repeatId: o.room_event.repeatId,
			        		title: date.room_event.title,
			        		start: new Date(st),//moment.js format
			        		end: new Date(en),
			        		room: o.room_event.room,
			        		createdBy: o.room_event.createdBy
		    			});
		    		   
		    	   });
		    	   
		    	   
		    	   
		       }
		       console.log(droppedEvents);
		       var revert = false;
		       
	    	   var numConflict = 0;
		       
	    	   ReservationDataOp
			    .getConflicts(droppedEvents)
			    .then(function(response){
			       		
		       		
		       		evt = response.data;
		       		
	       			evt.forEach(function(obj){
						
						ev = {
								id: (obj.repeatId == 0) ? undefined: obj.repeatId,
			    				title: obj.title+' - '+ obj.room.room,
			    				start: new Date(moment(obj.start).format('YYYY-MM-DD HH:mm:ss')),
			    				end: new Date(moment(obj.end).format('YYYY-MM-DD HH:mm:ss')),
			    				color: obj.room.color_code,
			    				room_event: obj,
				    				
				    		};
		    			$scope.conflictEvents.push(ev);

		    		});
		       		
	       			if(response.data.length> 0){
	       				revertFunc();
	       				console.log(evt);
	       			}else{
	       				
	       			  ReservationDataOp
	       			   .updateEvent(droppedEvents)
	       			   .then(function(response){
	       				  //console.log(response); 
	       			   })
	       			   .catch(function (error){
	       				   console.log(error);
	       			   });
	       			}
					
		       		
		       		
		       	})
		       	.catch(function(error){
		       		console.log(error);
		       	});
	    	   
		 }
		 
		 //date picker
		 $scope.startDate = new Date();
		 $scope.endDate = new Date();
		 
		 
		  $scope.dateTimeNow = function() {
			    $scope.date = new Date();
			  };
			  $scope.dateTimeNow();
			  
			  $scope.toggleMinDate = function() {
			    var minDate = new Date();
			    var maxDate = new Date();
			    // set to yesterday
			    minDate.setDate(minDate.getDate() - 1);
			    maxDate.setDate(maxDate.getDate() + 3);
			    $scope.dateOptions.minDate = $scope.dateOptions.minDate ? null : minDate;
//			    $scope.dateOptions.maxDate = $scope.dateOptions.maxDate ? null : maxDate;
			  };
			   
			  $scope.dateOptions = {
			    showWeeks: false,
			    startingDay: 0
			  };
			  
			  $scope.toggleMinDate();
			  
			  // Disable weekend selection
			  $scope.disabled = function(calendarDate, mode) {
			    return mode === 'day' && ( calendarDate.getDay() === 0 || calendarDate.getDay() === 6 );
			  };
			  
			  $scope.open = function($event,opened) {
			    $event.preventDefault();
			    $event.stopPropagation();
			    $scope.dateOpened = true;
			  };
			  
			  $scope.dateOpened = false;
			  $scope.hourStep = 1;
			  $scope.format = "MMM. dd, yyyy";
			  $scope.minuteStep = 1;
			  // add min-time="minTime" to datetimepicker to use this value 
			  $scope.minTime = new Date(0, 0, 0, Math.max(1, $scope.date.getHours() - 2), 0, 0, 0);

			  $scope.timeOptions = {
			    hourStep: [1, 2, 3],
			    minuteStep: [1, 5, 10, 15, 25, 30]
			  };

			  $scope.showMeridian = true;
			  $scope.timeToggleMode = function() {
			    $scope.showMeridian = !$scope.showMeridian;
			  };
			  
			  $scope.$watch("date", function(date) {
			    // read date value
			  }, true);
			  
			  $scope.resetHours = function() {
			    $scope.date.setHours(1);
			  };
			  
			  $scope.selectedRoom ={};
			  $scope.eventTitle ='';
			  
			  $scope.addEvent = function(){
				  
				  e = moment($scope.endDate);
				  s = moment($scope.startDate);
				  
				 if(s.diff(e) < 0 )  {
					 
					 events =[];
					 
					 events.push({
						title: $scope.eventTitle,
						start: new Date(s.format('YYYY-MM-DD HH:mm:ss')),
						end: new Date(e.format('YYYY-MM-DD HH:mm:ss')),
						room: $scope.selectedRoom,
						createdBy: {id:1}
					 });
					 
					 mondays = [];
					 daysOfWeek =[1,3,5];
					 
					 
					 
					 temp  = s.clone();
					 
					 mondays.push(temp.format('MMM-DD-YYYY'));
					 
					 while(temp.isBefore(e)){
						 
						 temp = temp.add(1,'days');
						
						// console.log(temp.weekday());
						 
						 for(i =0;i<daysOfWeek.length; i++){
							 //console.log(daysOfWeek[0]);
							 if(temp.weekday() === daysOfWeek[i]){
								 mondays.push(temp.format('MMM-DD-YYYY'));
							 }
						 }
						 
						 //mondays.push(temp.format('MMM-DD-YYYY'));
					 }
					 
					 console.log(mondays);
//					console.log(events);
//					 
//					ReservationDataOp
//						.addEvent(events)
//						.then(function(response){
//							console.log(response);
//						})
//						.catch(function(error){
//							console.log(error);
//						})
					 
				 }
			  }
		 
	}]);