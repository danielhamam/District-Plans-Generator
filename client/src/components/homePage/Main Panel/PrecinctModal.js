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
                    <b>{this.props.precinctName} </b>
                </div>
               <Table striped bordered hover size="sm" id="precinctModal" className="table">
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
                            <td>{this.props.featureObject.totalPopulation}</td>
                            <td>{this.props.featureObject.vaptotalPopulation}</td>
                        </tr>
                        <tr>
                            <th scope="row">White</th>
                            <td>{this.props.featureObject.whitePopulation}</td>
                            <td>{this.props.featureObject.whiteVAPPopulation}</td>
                        </tr>
                        <tr>
                            <th scope="row">African American</th>
                            <td>{this.props.featureObject.africanAmericanPopulation}</td>
                            <td>{this.props.featureObject.africanAmericanVAPPopulation}</td>
                        </tr>
                        <tr>
                            <th scope="row">Hispanic</th>
                            <td>{this.props.featureObject.hispanicPopulation}</td>
                            <td>{this.props.featureObject.hispanicVAPPopulation}</td>
                        </tr>
                        <tr>
                            <th scope="row">Asian</th>
                            <td>{this.props.featureObject.asianPopulation}</td>
                            <td>{this.props.featureObject.asianVAPPopulation}</td>
                        </tr>
                        <tr>
                            <th scope="row">American Indian</th>
                            <td>{this.props.featureObject.americanIndianPopulation}</td>
                            <td>{this.props.featureObject.americanIndianVAPPopulation}</td>
                        </tr>
                        <tr>
                            <th scope="row">Hawaiian</th>
                            <td>{this.props.featureObject.nativeHawaiianPopulation}</td>
                            <td>{this.props.featureObject.nativeHawaiianVAPPopulation}</td>
                        </tr>
                        <tr>
                            <th scope="row">Other</th>
                            <td>{this.props.featureObject.otherRacePopulation + this.props.featureObject.multipleRacePopulation} </td>
                            <td>{this.props.featureObject.otherRaceVAPPopulation + this.props.featureObject.multipleRaceVAPPopulation} </td>
                        </tr>
                    </tbody>
                </Table>
            </div>
            );
        }
}

export default PrecinctModal;