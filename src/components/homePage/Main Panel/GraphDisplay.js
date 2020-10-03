import Plot from 'react-plotly.js';
import React, {Component} from 'react';

class GraphDisplay extends Component {
    render() {
        return (
                <Plot
                  data={[
                {
                    x: [1, 2, 3],
                    y: [2, 6, 3],
                    type: 'scatter',
                    mode: 'lines+markers',
                    marker: {color: 'red'},
                },
                {type: 'bar', x: [1, 2, 3], y: [2, 5, 3]},
            ]}
            layout={ {width: 150, height: 150, title: 'Districting Plans'}}
            responsive={true}
      />
        );
    }
}

export default GraphDisplay;
