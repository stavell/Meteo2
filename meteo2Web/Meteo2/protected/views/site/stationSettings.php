<?php

    $this->widget('zii.widgets.grid.CGridView', array(
        'dataProvider' => new CArrayDataProvider(Settings::model()->findAll()),
        'columns' => array_keys(Settings::model()->metaData->columns)
    ));