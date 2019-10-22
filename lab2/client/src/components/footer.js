import React from 'react';
import config from '../shared/config';

function Footer() {
    return (
        <h4>
            <strong>version {config.app.version}</strong>
        </h4>
    )
}

export default Footer;
