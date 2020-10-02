import React, {Component} from 'react';
import BatchCard from './BatchCard';

// This class represents the list of Batches. This will handle "currently selected Batch" and 
// generally manage the batches. 

class YourBatches extends Component {

    render() {
        return (
    
            <div>
                <BatchCard />
                <BatchCard />
            </div>
            
        );
    }
}

export default YourBatches;