const tileServer = window.location.origin + process.env.VUE_APP_MAP_SERVER
const styleJson_Light = {
    version: 8,
    sources: {
        offlineMapTiles: {
            type: 'vector',
            tiles: [tileServer + '/proxy/tiles/world/{z}/{x}/{y}.pbf'],
            minzoom: 0,
            maxzoom: 22, //local TILES max zoom::12
        }
    },
    layers: [
        {
            id: 'water_polygon',
            type: 'fill',
            source: 'offlineMapTiles',
            'source-layer': 'water',
            minzoom: 0,
            maxzoom: 22,
            layout: {
                visibility: 'visible',
            },
            paint: {
                'fill-color': 'rgb(145, 183, 214)',
                'fill-opacity': 1.0,
            },
            filter: ['==', '$type', 'Polygon'],
        },
        {
            id: "landcover",
            type: 'fill',
            source: 'offlineMapTiles',
            'source-layer': "landcover",
            minzoom: 0,
            maxzoom: 22,
            layout: {
                visibility: 'visible',
            },
            paint: {
                'fill-color': 'rgb(228, 228, 228)',
                'fill-opacity': 1,
            },
            filter: ['==', '$type', 'Polygon'],
        }
    ],
    glyphs: '/glyphs/mapbox/{fontstack}/{range}.pbf',
}
const styleJson_Dark = {
    version: 8,
    sources: {
        offlineMapTiles: {
            type: 'vector',
            tiles: [tileServer + '/proxy/tiles/world/{z}/{x}/{y}.pbf'],
            minzoom: 0,
            maxzoom: 22, //local TILES max zoom::12
        }
    },
    layers: [
        {
            id: 'water_polygon',
            type: 'fill',
            source: 'offlineMapTiles',
            'source-layer': 'water',
            minzoom: 0,
            maxzoom: 22,
            layout: {
                visibility: 'visible',
            },
            paint: {
                'fill-color': 'rgb(81,124,142)',
                'fill-opacity': 1,
            },
            filter: ['==', '$type', 'Polygon'],
        },
        {
            id: 'transportation_line',
            type: 'line',
            source: 'offlineMapTiles',
            minzoom: 0,
            maxzoom: 22,
            'source-layer': 'transportation',
            layout: {
                visibility: 'visible',
            },
            paint: {
                'line-color': 'hsl(197, 100%, 50%)',
                'line-width': 1,
                'line-opacity': 0.1,
            },
            filter: [
                'all',
                ['==', ['geometry-type'], 'LineString'],
                [
                    'all',
                    ['!', ['has', 'access']],
                    [
                        'match',
                        ['get', 'class'],
                        [
                            'primary',
                            'primary_construction',
                            'secondary',
                            'secondary_construction',
                            'tertiary',
                        ],
                        true,
                        false,
                    ],
                ],
            ],
        },
        {
            id: "landcover",
            type: 'fill',
            source: 'offlineMapTiles',
            'source-layer': "landcover",
            minzoom: 0,
            maxzoom: 22,
            layout: {
                visibility: 'visible',
            },
            paint: {
                'fill-color': 'rgb(56,65,77)',
                'fill-opacity': 1,
            },
            filter: ['==', '$type', 'Polygon'],
        }
    ],
    glyphs: '/glyphs/mapbox/{fontstack}/{range}.pbf',
}

const styleLight = {
    styleJson: styleJson_Light,
    background: 'rgb(242,244,247)'
}
const styleDark = {
    styleJson: styleJson_Dark,
    background: 'rgb(51,59,69)'
}

export {
    styleLight, styleDark
}