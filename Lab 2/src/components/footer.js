import React from 'react';
import config from '../shared/config';

console.log(config);

function Footer() {
    return (
        <h4>
            <strong>version {config.app.version}</strong>
        </h4>
    )
}

export default Footer;
