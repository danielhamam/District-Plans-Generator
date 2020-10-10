import HomeScreen from "./components/homePage/HomeScreen";
import React, { Component } from "react";
import 'bootstrap/dist/css/bootstrap.min.css';
// import 'react-pro-sidebar/dist/css/styles.css';
import './css/advanced_styles.scss';

// App.js is the parent component
class App extends Component {
    state = {
      batchCards : [],
      selectedBatchCheck: false,
      selectedBatchName : "", // take name of selected district
      selectedPlanCheck: false,
      selectedPlanName : "", // take name of selected district
      // todoLists: testTodoListData.todoLists, // Portion of my code taken from CSE 316
    }

  createBatch = () => {
    
  }

  deleteBatch = () => {

  }

  changeSelectedBatchName = () => {

  }

  changeSelectedPlanName = () => {
    
  }
  
  toggleSelectedBatchCheck = () => {
    if (this.state.selectedBatchCheck == false) this.setState({selectedBatchCheck : true});
    else this.setState({selectedBatchCheck : false});
  }

  toggleSelectedPlanCheck = () => {
    if (this.state.selectedPlanCheck == false) this.setState({selectedPlanCheck: true});
    else this.setState({selectedPlanCheck : false});
}

  render() {
  return (
        <HomeScreen selectedPlanCheck={this.state.selectedPlanCheck} toggleSelectedPlanCheck={this.toggleSelectedPlanCheck} selectedBatchCheck={this.state.selectedBatchCheck} toggleSelectedBatchCheck={this.toggleSelectedBatchCheck}/>
    );
  }
}

export default App;
