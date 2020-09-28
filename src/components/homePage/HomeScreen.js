import Navbar from "../navbar/Navbar";
import Footer from "../footer/Footer";
import React, { Component } from "react";

export class HomeScreen extends Component {

  render() {
    return (
      <div id="home_screen">
          <Navbar />
          <Footer />
      </div>
    );
  }
}

export default HomeScreen;