<template>
    <div class="main">
        <div id="map" ref="container"></div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import mapboxgl, { Projection } from 'mapbox-gl';
import 'mapbox-gl/dist/mapbox-gl.css'

const tileServer = window.location.origin + process.env.VUE_APP_MAP_SERVER
const styleJson1 = {
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
const styleJson2 = {
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


const container = ref(null)
onMounted(() => {
    console.log(tileServer)
    // container.value.style.backgroundColor = 'rgb(242,244,247)' // styleJson1
    container.value.style.backgroundColor = 'rgb(51,59,69)' // styleJson2
    const map = new mapboxgl.Map({
        container: container.value! as HTMLElement,
        accessToken: 'pk.eyJ1IjoibnVqYWJlc2xvbyIsImEiOiJjbGp6Y3czZ2cwOXhvM3FtdDJ5ZXJmc3B4In0.5DCKDt0E2dFoiRhg3yWNRA',
        // style: 'mapbox://styles/nujabesloo/clyp5nxqt019401pigyrh6wbs',
        // style: styleJson1 as any,
        style: styleJson2 as any,
        center: [115, 33],
        zoom: 6,
        projection: "mercator" as any,
    })
})
</script>

<style lang="scss" scoped>
.main {
    position: absolute;
    width: 100vw;
    height: 100vh;
    background-color: #ffffff;

    #map {
        position: relative;
        width: 100%;
        height: 100%;
    }
}
</style>