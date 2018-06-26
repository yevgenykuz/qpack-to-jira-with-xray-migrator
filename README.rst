QPACK to JIRA with XRAY Migrator
################################

Use to migrate from Orcanos *QPACK* to *JIRA* with the *XRAY* addon.

-----


.. contents::

.. section-numbering::


Usage
=====

Clone the source code
---------------------

In your working folder (your home folder, for example)

.. code-block:: bash

    git clone https://github.com/yevgenykuz/qpack-to-jira-with-xray-migrator.git

Configure your project
----------------------

Rename *"dummy.properties"* to *"configuration.properties"* and fill your QPACK and Jira parameters and input .xlsx
file name

Compile with Maven
------------------

.. code-block:: bash

    mvn clean install

Run it
------

Replace `example .xlsx input file <https://github.com/yevgenykuz//qpack-to-jira-with-xray-migrator/blob/master/TC_List.xlsx>`_ with your own, and then:

.. code-block:: bash

    mv target/qpack-to-jira-with-xray-migrator-standalone.jar .
    java -jar qpack-to-jira-with-xray-migrator-standalone.jar

Features
========

QPACK *"Test Case"* -> XRAY *"Test"* migration
----------------------------------------------

* **.xlsx input** - Export an .xlsx file with keys (IDs) of all your *"Test Cases"* and use it as input for this tool. See `example .xlsx file <https://github.com/yevgenykuz//qpack-to-jira-with-xray-migrator/blob/master/TC_List.xlsx>`_
* **Maintain project structure** - QPACK project structure will be documented into a unique JIRA field to be used later (by addons like *"Structure"*, for example)
* **Link back to QPACK** - Each XRAY *"Test"* will contain a link to the QPACK *"Test Case"* it was copied from
* **Transfer images** - Images from QPACK *"Test Case"* description field will be uploaded and embedded into JIRA *"Test"*  description field
* **TestCase <-> Test Mapping** - An output .xlsx mapping file is produced, with IDs and links to both QPACK and JIRA


Meta
====

Authors
-------

`yevegnykuz <https://github.com/yevegnykuz>`_

License
-------

BSD-3-Clause - `LICENSE <https://github.com/yevgenykuz//qpack-to-jira-with-xray-migrator/blob/master/LICENSE>`_


-----