<template>
    <div class="scene-map-wrapper" :order="$props.order">
        <div id="map">
            <div id="popUp" style="width:100px; height: 100px; z-index: 99; position: relative;"></div>
        </div>
    </div>
</template>
  
  
<script setup lang='ts'>
import mapboxgl from 'mapbox-gl';
import 'mapbox-gl/dist/mapbox-gl.css';
import { onMounted } from '@vue/runtime-core';
import { MapDataPreparer } from '@/utils/viewerData';


interface Props {
    mapId: string,
    order: string,
    projectId: string
}

const props = defineProps<Props>();

const mapDataPreparer = new MapDataPreparer();

const dashArraySequence = [
    [0, 4, 3],
    [0.5, 4, 2.5],
    [1, 4, 2],
    [1.5, 4, 1.5],
    [2, 4, 1],
    [2.5, 4, 0.5],
    [3, 4, 0],
    [0, 0.5, 3, 3.5],
    [0, 1, 3, 3],
    [0, 1.5, 3, 2.5],
    [0, 2, 3, 2],
    [0, 2.5, 3, 1.5],
    [0, 3, 3, 1],
    [0, 3.5, 3, 0.5]
];
let step = 0;



onMounted(async () => {
    // mapbox key
    mapboxgl.accessToken = 'pk.eyJ1Ijoiam9obm55dCIsImEiOiJja2xxNXplNjYwNnhzMm5uYTJtdHVlbTByIn0.f1GfZbFLWjiEayI6hb_Qvg';

    const map = new mapboxgl.Map(
        {
            container: 'map',
            style: 'mapbox://styles/johnnyt/clld6armr00f901q0dyqh7452',
            center: [120.861, 31.8813],
            zoom: 11.16,
            pitch:15, 
            bearing: 20
        }
    )
    
    // const popUp = new mapboxgl.Popup().setDOMContent(testPopIns.$el);
    let floatLineData = await mapDataPreparer.prepareFloatLineDataSource(props.projectId);

    let sectionData = await mapDataPreparer.prepareSectionDataSource(props.projectId);

    function animateDashArray(timestamp: any) {
        // Update line-dasharray using the next value in dashArraySequence. The
        // divisor in the expression `timestamp / 50` controls the animation speed.
        const newStep = Math.floor((timestamp / 100) % dashArraySequence.length);

        if (newStep !== step) {
            // console.log(step)
            // console.log(1);
            for(let i = 0; i<mapDataPreparer.floatPtNum; i++) {
                map.setPaintProperty(
                    'floatLine' + i + '-dashed',
                    'line-dasharray', 
                    dashArraySequence[step]
                );
            }
            step = newStep;
        }

        // Request the next frame of the animation.
        requestAnimationFrame(animateDashArray);
    }

    if (map.loaded()) {
        mapDataPreparer.addSectionLayers(map, sectionData);
        mapDataPreparer.addFloatLineLayers(map, floatLineData);
        animateDashArray(0);
    }
    else {
        map.on("load", () => {
            mapDataPreparer.addSectionLayers(map, sectionData);
            mapDataPreparer.addFloatLineLayers(map, floatLineData);
            animateDashArray(0);
        })
    }
    // map.on('drag', () => {
    //     console.log('zoom',map.getZoom())
    //     console.log('b',map.getBearing())
    //     console.log('p',map.getPitch())
    //     console.log('c',map.getCenter())
    // })

    

});

</script>
  
<style lang='scss'>
$orders: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10;

div.scene-map-wrapper {
    cursor: pointer;
    background-color: transparent;
    backdrop-filter: blur(8px);
    border-radius: 4px;
    width: 42%;
    height: 66%;

    div#map {
        border-radius: 4px;
        width: 100%;
        height: 100%;
        background: rgba(255, 255, 255, 0.2);
    }

    @each $order in $orders {
        &[order='#{$order}'] {
            order: $order;
        }
    }
}
</style>