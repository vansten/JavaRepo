angular.module('hello', [])
    .controller('home', function($scope){
        $scope.greeting = {id: '666', content: 'Hello world!'}
    })