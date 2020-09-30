import HomeScreen from "./components/homePage/HomeScreen";
import React, { Component } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-pro-sidebar/dist/css/styles.css';

// App.js is the parent component
class App extends Component {
  render() {
  return (
        <HomeScreen />
    );
  }
}

export default App;
