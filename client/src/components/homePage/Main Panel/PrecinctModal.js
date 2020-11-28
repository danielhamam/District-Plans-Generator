import React, { Component } from 'react';
import Table from 'react-bootstrap/Table'

class PrecinctModal extends Component {
    constructor () {
        super();
        this.state = {

        }

    }

    render() {
        
        return (
            <div id="precinctModalWrapper" style={this.props.togglePrecinctModal ? { display : 'block' } : { display: 'none' }} >
               <div id="precinctName">
                    <b> Precinct Name: </b>
                    Example
                </div>
               <Table striped bordered hover id="precinctModal" className="table">
                    <thead>
                        <tr>
                            <th scope="col">Demographic Category</th>
                            <th scope="col">Overall Population </th>
                            <th scope="col">Voting Age Population </th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <th scope="row">Total</th>
                            <td>1,000,000</td>
                            <td>400,000</td>
                        </tr>
                        <tr>
                            <th scope="row">African American</th>
                            <td>1,000,000</td>
                            <td>300,000</td>
                        </tr>
                        <tr>
                            <th scope="row">Hispanic</th>
                            <td>500,000</td>
                            <td>300,000</td>
                        </tr>
                        <tr>
                            <th scope="row">Asian</th>
                            <td>500,000</td>
                            <td>200,000</td>
                        </tr>
                        <tr>
                            <th scope="row">American Indian</th>
                            <td>500,000</td>
                            <td>100,000</td>
                        </tr>
                        <tr>
                            <th scope="row">Hawaiian</th>
                            <td>500,000</td>
                            <td>250,000</td>
                        </tr>
                        <tr>
                            <th scope="row">Other</th>
                            <td>500,000</td>
                            <td>250,000</td>
                        </tr>
                    </tbody>
                </Table>
            </div>
            );
        }
}

export default PrecinctModal;