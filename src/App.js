import HomeScreen from "./components/homePage/HomeScreen";
import React, { Component } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';

// App.js is the parent component
class App extends Component {
  render() {
  return (
      <div >
        <HomeScreen />
      </div>
    );
  }
}

export default App;
